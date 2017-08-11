package com.ran.kolibri.repository.account

import com.ran.kolibri.entity.financial.Account
import org.springframework.data.repository.CrudRepository

interface AccountRepository : CrudRepository<Account, Long>