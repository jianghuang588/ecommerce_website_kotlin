package com.Admin.service.impl

import com.Admin.domain.Shirt
import com.Admin.repository.ShirtRepository
import com.Admin.service.ShirtService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class ShirtServiceImpl : ShirtService {

    @Autowired
    private val shirtRepository: ShirtRepository? = null

    override fun save(shirt: Shirt?): Shirt? {
        return shirt?.let { shirtRepository!!.save(it) }
    }

    override fun findAll(): List<Shirt?> {
        return shirtRepository!!.findAll() as List<Shirt?>
    }

    override fun findOne(id: Long?): Shirt {
        return shirtRepository!!.findById(id!!).orElse(null)!!
    }


    override fun removeOne(id: Long?) {
        shirtRepository!!.deleteById(id!!)
    }



}
