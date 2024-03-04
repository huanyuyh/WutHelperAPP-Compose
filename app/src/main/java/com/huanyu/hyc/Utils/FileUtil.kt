package com.huanyu.hyc.Utils

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileReader
import java.io.FileWriter
import java.nio.charset.StandardCharsets

class FileUtil {
    companion object{
        public fun saveText(path: String,text: String){
            var os: BufferedWriter? = null
            os = BufferedWriter(FileWriter(path))
            os?.write(text)
            os?.close()
        }

        public fun readText(path: String): String? {
            var ins:BufferedReader? =null
            ins = BufferedReader(FileReader(path))
            var text = ins?.readText()
            ins?.close()
            return text
        }

    }
}