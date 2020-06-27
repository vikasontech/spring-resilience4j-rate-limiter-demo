package com.example.springresillencerjdemo


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono


@SpringBootApplication
class SpringResilienceDemoApplication

@RestController
class SomeController {

  @Autowired
  private lateinit var someService: SomeService

  @GetMapping("/process", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
  fun somRequest(): Mono<String> {
    return someService.process()
  }
}

@Service
class SomeService {

  @RateLimiter(name="processService", fallbackMethod = "processFallback")
  fun process(): Mono<String> {
    return Mono.just("ah what do you want ...")
  }

  fun processFallback(exp: Throwable): Mono<String> {
    log.error("eh!!! this is the error ${exp.localizedMessage}")
    return Mono.just("inside from fallback method because `${exp.localizedMessage}`")
  }

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }
}

fun main(args: Array<String>) {
  runApplication<SpringResilienceDemoApplication>(*args)
}
