package com.traffic.report.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.traffic.report.model.Traffic;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class ElasticSearchTrafficInfoRepositoryImp implements ElasticSearchTrafficInfoRepository {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Autowired
    private ObjectMapper objectMapper;

    static private final String INDEX = "network_packet";

    @Override
    public List<Traffic> findTrafficInfo(String userId, String hostName, String fromDate, String toDate) {

        final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(INDEX);
        searchRequest.scroll(scroll);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        if(!fromDate.isEmpty() && toDate.isEmpty())
            toDate = fromDate;

        if(fromDate.isEmpty() && !hostName.isEmpty())
            searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("user_id.keyword", userId))
                                                           .must(QueryBuilders.wildcardQuery("url",String.format("*%s*",hostName))));
        else if(!fromDate.isEmpty() && hostName.isEmpty()){
            searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("user_id.keyword", userId))
                    .must(QueryBuilders.rangeQuery("localdate").from(fromDate).to(toDate)));
        }else if(fromDate.isEmpty() && hostName.isEmpty()){
            searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("user_id.keyword", userId)));
        }
        else
            searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("user_id.keyword", userId))
                    .must(QueryBuilders.wildcardQuery("url", String.format("*%s*",hostName)))
                    .must(QueryBuilders.rangeQuery("localdate").from(fromDate).to(toDate)));
        searchSourceBuilder.size(10000);
        searchRequest.source(searchSourceBuilder);
        List<Traffic> traffics = new ArrayList<>();
        SearchResponse searchResponse = null;

        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            String scrollId = searchResponse.getScrollId();
            SearchHit[] searchHits = searchResponse.getHits().getHits();
            while (searchHits != null && searchHits.length > 0) {
                for (SearchHit hit : searchHits) {
                    Map<String, Object> map = hit.getSourceAsMap();
                    traffics.add(objectMapper.convertValue(map, Traffic.class));
                }
                SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
                scrollRequest.scroll(scroll);
                searchResponse = restHighLevelClient.scroll(scrollRequest, RequestOptions.DEFAULT);
                scrollId = searchResponse.getScrollId();
                searchHits = searchResponse.getHits().getHits();
            }
            ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
            clearScrollRequest.addScrollId(scrollId);
            ClearScrollResponse clearScrollResponse = restHighLevelClient.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
            boolean succeeded = clearScrollResponse.isSucceeded();

        } catch (IOException e) {
            e.printStackTrace();
        }
        traffics.removeIf(t -> t.getUrl().trim().isEmpty());
        return traffics;
    }


}
