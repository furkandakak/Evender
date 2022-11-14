package com.furkandakak.together.spinners
public class SpinnerCategoryArray {

    val category_array= arrayOf("Kategori Seçiniz:",
        "ONLINE",
        "Eğlence",
        "Müzik",
        "Konser",
        "Tiyatro",
        "Sinema",
        "Sergi",
        "Sanat",
        "Gezi",
        "Müze",
        "Sosyal Sorumluluk",
        "Eğitim",
        "Ders Çalışma",
        "Kişisel Gelişim",
        "Yeme-İçme",
        "Spor",
        "Oyun(ONLINE)",
        "Oyun",
        "Kız Kıza",
        "Erkek Erkeğe",
        "DİĞER")

    fun getList(): Array<String>
    {
        return category_array
    }


}
