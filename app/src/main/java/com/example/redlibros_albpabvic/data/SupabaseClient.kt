package com.example.redlibros_albpabvic.data


import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest


object SupabaseClient {
    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = "https://iackhhtubsqweuttojpe.supabase.co",
        supabaseKey = "sb_publishable_droxdltXWqL9pb3gnQ4ubg_EHNqJxfr"
    ) {
        install(Postgrest)
        install(Auth)
    }
}