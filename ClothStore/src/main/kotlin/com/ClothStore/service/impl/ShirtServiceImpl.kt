package com.ClothStore.service.impl

import com.ClothStore.domain.Shirt
import com.ClothStore.repository.ShirtRepository
import com.ClothStore.service.ShirtService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class ShirtServiceImpl : ShirtService {
    @Autowired
    private val clothingRepository: ShirtRepository? = null

    override fun findAll(): List<Shirt?>? {
        val clothingList = clothingRepository!!.findAll() as List<Shirt?>
        val availableShirtList: MutableList<Shirt?> = ArrayList()

        for (cloth in clothingList) {
            if (cloth!!.isActive) {
                availableShirtList.add(cloth)
            }
        }

        return availableShirtList
    }

    override fun findOne(identity: Long?): Shirt {
        return clothingRepository!!.findById(identity!!).orElse(null)!!
    }

    override fun findByCategory(kind: String?): List<Shirt> {
        val shirtSelection: List<Shirt?>? = clothingRepository?.findByCategory(kind)

        val availableShirts: MutableList<Shirt> = ArrayList()

        for (cloth in shirtSelection!!) {
            if (cloth?.isActive == true) {
                availableShirts.add(cloth)
            }
        }

        return availableShirts
    }


    override fun blurrySearch(header: String?): List<Shirt?> {
        val shirtKind = clothingRepository!!.findByTitleContaining(header)
        val availableShirtCollection: MutableList<Shirt?> = ArrayList()

        for (shirt in shirtKind!!) {
            if (shirt!!.isActive) {
                availableShirtCollection.add(shirt)
            }
        }

        return availableShirtCollection
    }


}
