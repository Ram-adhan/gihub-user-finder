package com.example.githubuserfinder.utils

fun String.removeSpace(): String =
    replace("\\s+".toRegex(), "+")
