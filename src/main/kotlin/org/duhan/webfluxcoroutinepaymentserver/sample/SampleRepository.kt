package org.duhan.webfluxcoroutinepaymentserver.sample

import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface SampleRepository: CoroutineCrudRepository<Sample, Long>