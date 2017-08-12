package com.ran.kolibri.repository.financial

import com.ran.kolibri.entity.financial.Account
import org.springframework.data.repository.CrudRepository

interface AccountRepository : CrudRepository<Account, Long>