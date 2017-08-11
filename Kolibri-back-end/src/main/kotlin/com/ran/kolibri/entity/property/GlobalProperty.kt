package com.ran.kolibri.entity.property

import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "global_property")
class GlobalProperty : Property()