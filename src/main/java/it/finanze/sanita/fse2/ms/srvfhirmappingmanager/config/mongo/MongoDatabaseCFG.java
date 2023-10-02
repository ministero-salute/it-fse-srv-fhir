/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 * 
 * Copyright 2023 Ministero della Salute
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.mongo;


import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
 

/**
 *	Configuration for MongoDB.
 */
@Configuration
public class MongoDatabaseCFG {
 
    final List<Converter<?, ?>> conversions = new ArrayList<>();

    @Bean
    public MongoDatabaseFactory mongoDatabaseFactory(MongoPropertiesCFG mongoProp){
    	  ConnectionString connectionString = new ConnectionString(mongoProp.getUri());
          MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
              .applyConnectionString(connectionString)
              .build();
          return new SimpleMongoClientDatabaseFactory(MongoClients.create(mongoClientSettings), mongoProp.getSchemaName());
    }

    @Bean
    @Primary
    public MongoTemplate mongoTemplate(MongoDatabaseFactory factory, ApplicationContext appContext) {
        // Assign application context to mongo
        final MongoMappingContext mongoMappingContext = new MongoMappingContext();
        mongoMappingContext.setApplicationContext(appContext);
        // Apply default mapper
        MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(factory),mongoMappingContext);
        // Set the default type mapper (removes custom "_class" column)
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        // Return the new instance
        return new MongoTemplate(factory, converter);
    }
}