package org.motivepick.repository

import org.motivepick.domain.entity.LoginStateEntity
import org.springframework.data.repository.PagingAndSortingRepository
import java.util.*

interface LoginStateRepository : PagingAndSortingRepository<LoginStateEntity, Long> {

    fun findByStateUuid(uuid: String): Optional<LoginStateEntity>

    fun deleteByStateUuid(uuid: String): Optional<LoginStateEntity>
}
