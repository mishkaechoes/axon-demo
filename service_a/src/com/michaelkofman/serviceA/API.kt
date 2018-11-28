package com.michaelkofman.serviceA

import com.michaelkofman.dummy.DummyEvent
import org.axonframework.commandhandling.callbacks.LoggingCallback
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventhandling.EventBus
import org.axonframework.eventhandling.EventHandler
import org.axonframework.messaging.responsetypes.MultipleInstancesResponseType
import org.axonframework.queryhandling.QueryGateway
import org.axonframework.queryhandling.QueryHandler
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.*
import java.util.concurrent.Future
import javax.annotation.PostConstruct
import javax.persistence.Entity
import javax.persistence.Id

@Component
internal class RandomHandler() {
    @PostConstruct
    fun successfulConstruction() {
        println("RandomHandler Created")
    }

    @EventHandler
    fun handle(event: DummyEvent) {
        println("DummyEvent Received EVENT ${event.id}")
    }

    @EventHandler
    fun handle(event: Object) {
        println("Event Spy ${event.`class`}")
    }
}

@RestController
class CommandController(private val eventBus: EventBus, private val commandGateway: CommandGateway) {
    @PostMapping(value = "/command/dummy")
    @ResponseStatus(value = HttpStatus.CREATED)
    fun makeDummy(): String {
        val randomId = UUID.randomUUID().toString()
        commandGateway.send(DummyCommand(name = randomId), LoggingCallback.INSTANCE)
        return randomId
    }
}

@RestController
class QueryController(private val queryGateway: QueryGateway) {
    @GetMapping(value = "/dummies")
    fun all(): Future<List<DummyEntity>> {
        return queryGateway.query(AllDummies(), MultipleInstancesResponseType(DummyEntity::class.java))
    }
}

class AllDummies

@Component
class DummyProjection(private val dummyRepository: DummyRepository) {
    @QueryHandler
    fun on(query: AllDummies): List<DummyEntity> {
        return dummyRepository.findAll()
    }

    @EventHandler
    fun on(event: DummyEvent) {
        dummyRepository.save(DummyEntity(event.id, event.value))
    }
}

@Entity
class DummyEntity(@Id val id: String, val value: String)

interface DummyRepository: JpaRepository<DummyEntity, String>