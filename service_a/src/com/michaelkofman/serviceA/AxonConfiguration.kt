package com.michaelkofman.serviceA

import org.axonframework.commandhandling.CommandBus
import org.axonframework.eventsourcing.eventstore.jdbc.EventSchema
import org.axonframework.eventsourcing.eventstore.jdbc.EventTableFactory
import org.axonframework.eventsourcing.eventstore.jdbc.PostgresEventTableFactory
import org.axonframework.messaging.interceptors.BeanValidationInterceptor
import org.axonframework.modelling.saga.repository.jdbc.PostgresSagaSqlSchema
import org.axonframework.modelling.saga.repository.jdbc.SagaSqlSchema
import org.axonframework.spring.eventsourcing.SpringAggregateSnapshotterFactoryBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AxonFrameworkConfiguration {
    @Autowired
    fun registerInterceptors(commandBus: CommandBus) {
        commandBus.registerDispatchInterceptor(BeanValidationInterceptor())
    }

    @Bean
    fun snapshotterFactoryBean() = SpringAggregateSnapshotterFactoryBean()

    @Bean
    fun eventSchemaFactory(): EventTableFactory {
        return PostgresEventTableFactory.INSTANCE
    }

    @Bean
    fun eventSchema(): EventSchema {
        return EventSchema()
    }

    @Bean
    fun sagaSqlSchema(): SagaSqlSchema {
        return PostgresSagaSqlSchema()
    }
}