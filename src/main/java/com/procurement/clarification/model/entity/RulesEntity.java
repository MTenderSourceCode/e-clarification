package com.procurement.clarification.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("clarification_rules")
@Getter
@Setter
public class RulesEntity {
    @PrimaryKeyColumn(name = "country", type = PrimaryKeyType.PARTITIONED)
    private String country;

    @PrimaryKeyColumn(name = "pmd", type = PrimaryKeyType.CLUSTERED)
    private String method;

    @PrimaryKeyColumn(name = "parameter", type = PrimaryKeyType.CLUSTERED)
    private String parameter;

    @Column("value")
    private String value;
}
