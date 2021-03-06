package org.motivepick

import com.github.springtestdbunit.DbUnitTestExecutionListener
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener
import org.springframework.boot.test.mock.mockito.ResetMocksTestExecutionListener
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import org.springframework.test.context.support.DirtiesContextTestExecutionListener
import org.springframework.test.context.transaction.TransactionalTestExecutionListener
import javax.transaction.Transactional

@Retention(AnnotationRetention.RUNTIME)
@ActiveProfiles("test")
@SpringBootTest
@Transactional
@TestExecutionListeners(
        DbUnitTestExecutionListener::class,
        ResetMocksTestExecutionListener::class,
        DependencyInjectionTestExecutionListener::class,
        DirtiesContextTestExecutionListener::class,
        TransactionalTestExecutionListener::class,
        MockitoTestExecutionListener::class,
        OAuth2TestExecutionListener::class
)
annotation class IntegrationTest(val userAccountId: Long, val userName: String)
