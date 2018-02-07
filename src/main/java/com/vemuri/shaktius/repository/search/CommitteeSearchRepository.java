package com.vemuri.shaktius.repository.search;

import com.vemuri.shaktius.domain.Committee;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Committee entity.
 */
public interface CommitteeSearchRepository extends ElasticsearchRepository<Committee, Long> {
}
