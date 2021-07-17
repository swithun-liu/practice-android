package com.swithun.myapplication

class Solution {
    fun displayTable(orders: List<List<String>>): List<List<String>> {
        val table = mutableListOf<MutableList<String>>(mutableListOf<String>("table"))
        val mp = mutableMapOf<String, Int>()
        for ((cus, tab, food) in orders) {
            if (!table[0].contains(food)) {
                table[0].add(food)
            }
            println(table)
            if (!mp.contains(tab)) {
                mp.put(tab, table.size)
                table.add(mutableListOf(tab))
            }
            for (row in table) {
                while (row.size < table[0].size) {
                    row.add("0")
                }
            }
            println(table)
            println("第${table[0].indexOf(food)}个")
            table[mp[tab]!!][table[0].indexOf(food)] =
                (table[mp[tab]!!][table[0].indexOf(food)].toInt() + 1).toString()
        }
        println(table)
        return table
    }
}


