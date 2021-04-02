package com.traffic.report.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.traffic.report.model.TrafficInfo;
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
public class TrafficInfoRepositoryImp implements TrafficInfoRepository{

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Autowired
    private ObjectMapper objectMapper;

    static private final String INDEX = "network_packet";

    @Override
    public List<TrafficInfo> findTrafficInfo(String userId, String hostName, String date) {
        final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(INDEX);
        searchRequest.scroll(scroll);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        if(date.isEmpty() && !hostName.isEmpty())
            searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("user_id.keyword", userId))
                                                           .must(QueryBuilders.wildcardQuery("url",String.format("*%s*",hostName))));
        else if(!date.isEmpty() && hostName.isEmpty()){
            searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("user_id.keyword", userId))
                    .must(QueryBuilders.rangeQuery("localdate").from(date).to(date)));
        }else if(date.isEmpty() && hostName.isEmpty()){
            searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("user_id.keyword", userId)));
        }
        else
            searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("user_id.keyword", userId))
                    .must(QueryBuilders.wildcardQuery("url", String.format("*%s*",hostName)))
                    .must(QueryBuilders.rangeQuery("localdate").from(date).to(date)));
        log.info(searchSourceBuilder.toString());
        searchSourceBuilder.size(15);
        searchRequest.source(searchSourceBuilder);
        List<TrafficInfo> trafficsInfo = new ArrayList<>();
        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            String scrollId = searchResponse.getScrollId();
            SearchHit[] searchHits = searchResponse.getHits().getHits();
            while (searchHits != null && searchHits.length > 0) {
                for (SearchHit hit : searchHits) {
                    Map<String, Object> map = hit.getSourceAsMap();
                    trafficsInfo.add(objectMapper.convertValue(map, TrafficInfo.class));
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
            log.info("Scroll findByUserIDHostName status: "+String.valueOf(succeeded));
        } catch (IOException e) {
            e.printStackTrace();
        }
        trafficsInfo.removeIf(t -> t.getUrl().trim().isEmpty());
        return trafficsInfo;
    }


}
