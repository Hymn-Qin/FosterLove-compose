package com.example.foster.data.post.impl

import com.example.foster.R
import com.example.foster.model.Metadata
import com.example.foster.model.PetPal

val pal1 = PetPal(
    id = "11aa123ada",
    name = "Sandbags/沙包",
    imageId = R.drawable.siberian_husky,
    breeds = "Siberian Husky/哈士奇",
    metadata = Metadata(
        "Girl",
        3,
        "中文名：哈士奇\n" +
                "英文名：Siberian Husky\n" +
                "别名：西伯利亚雪撬犬\n" +
                "生源地：西伯利亚 \n" +
                "智商：第45名 \n" +
                "体型：中型犬 \n" +
                "肩高：51-60厘米\n" +
                "体重：16-27公斤 \n" +
                "毛长：长毛 \n" +
                "功能：伴侣犬、雪橇竞赛犬 \n" +
                "饲养：好养\n"
    ),
)

val pal2 = PetPal(
    id = "22aa123ada",
    name = "Snow/白雪",
    imageId = R.drawable.poodle,
    breeds = "Poodle/贵宾犬",
    metadata = Metadata(
        "Girl",
        2,
        "中文名：贵宾犬\n" +
                "英文名：Poodle\n" +
                "别名：贵妇犬、卷毛狗\n" +
                "生源地：法国 \n" +
                "智商：第2名 \n" +
                "体型：小型犬 \n" +
                "肩高：20-38厘米\n" +
                "体重：4-8公斤 \n" +
                "毛长：长毛 \n" +
                "功能：玩赏犬、伴侣犬、狩猎犬\n"
    ),
)

val pal3 = PetPal(
    id = "33aa123ada",
    name = "Banana/香蕉",
    imageId = R.drawable.golden_retriever,
    breeds = "Golden Retriever/金毛寻回犬",
    metadata = Metadata(
        "Boy",
        3,
        "中文名：金毛寻回犬\n" +
                "英文名：Golden Retriever\n" +
                "别名：金毛、金毛寻回猎犬、黄金猎犬、黄金拾猎犬\n" +
                "生源地：英国 \n" +
                "智商：第4名 \n" +
                "体型：大型犬 \n" +
                "肩高：55-61厘米\n" +
                "体重：27-34公斤 \n" +
                "毛长：长毛 \n" +
                "功能：宠物犬、寻回犬、单猎犬、导盲犬\n"
    )
)

val pal4 = PetPal(
    id = "44aa123ada",
    name = "Knight/骑士",
    imageId = R.drawable.german_shepherd_dog,
    breeds = "German Shepherd Dog/德国牧羊犬",
    metadata = Metadata(
        "Boy",
        7,
        "中文名:德国牧羊犬\n" +
                "英文名：German Shepherd Dog\n" +
                "别名：狼狗、德牧、黑背\n" +
                "生源地：法国 \n" +
                "智商：第3名 \n" +
                "体型：超大型犬 \n" +
                "肩高：55-56厘米\n" +
                "体重：34-43公斤 \n" +
                "毛长：短毛 \n" +
                "功能：军犬、警犬、搜救犬、导盲犬、牧羊犬、观赏犬、宠物犬\n"
    ),
)

val pal5 = PetPal(
    id = "55aa123ada",
    name = "Meat meat/肉肉",
    imageId = R.drawable.chinese_shar_pel,
    breeds = "Chinese Shar Pei/中国沙皮犬",
    metadata = Metadata(
        "Boy",
        6,
        "中文名：中国沙皮犬\n" +
                "英文名：Chinese Shar Pei\n" +
                "别名：大沥犬、打（斗）犬、中国斗狗\n" +
                "生源地：中国 \n" +
                "智商：第51名 \n" +
                "体型：中型犬 \n" +
                "肩高：46-51厘米\n" +
                "体重：16-20公斤 \n" +
                "毛长：短毛\n" +
                "功能：枪猎犬、看家犬、护卫犬\n"
    ),
)

val PET_PALS: List<PetPal> =
    listOf(
        pal1,
        pal2,
        pal3,
        pal4,
        pal5,
        pal1.copy(id = "66aa123ada", name = "Hunter/猎手"),
        pal2.copy(id = "77aa123ada", name = "Jasmine/茉莉"),
        pal3.copy(id = "88aa123ada", name = "Captain/船长"),
        pal4.copy(id = "99aa123ada", name = "Lightning/闪电"),
        pal5.copy(id = "12aa123ada", name = "King/国王"),
    )