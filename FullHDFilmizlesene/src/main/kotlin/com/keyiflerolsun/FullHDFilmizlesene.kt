// ! Bu araç @keyiflerolsun tarafından | @KekikAkademi için yazılmıştır.

package com.keyiflerolsun

import android.util.Log
import android.util.Base64
import org.jsoup.nodes.Element
import org.jsoup.nodes.Document
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.LoadResponse.Companion.addActors
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.M3u8Helper
import com.lagradost.cloudstream3.utils.loadExtractor

class FullHDFilmizlesene : MainAPI() {
    override var mainUrl            = "https://www.fullhdfilmizlesene.pw"
    override var name               = "FullHDFilmizlesene"
    override val hasMainPage        = true
    override var lang               = "tr"
    override val hasQuickSearch     = true
    override val hasDownloadSupport = true
    override val supportedTypes     = setOf(TvType.Movie)

    override val mainPage           =
        mainPageOf(
            "$mainUrl/en-cok-izlenen-filmler-izle-hd/" to "En Çok izlenen Filmler",
            "$mainUrl/filmizle/imdb-puani-yuksek-filmler-izle-1/" to "IMDB Puanı Yüksek Filmler",
            "$mainUrl/filmizle/bilim-kurgu-filmleri-izle-1/" to "Bilim Kurgu Filmleri",
            "$mainUrl/filmizle/komedi-filmleri-izle-2/" to "Komedi Filmleri",
        )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val document = app.get(request.data + page).document
        val home     = document.select("li.film").mapNotNull { it.toSearchResult() }

        return newHomePageResponse(request.name, home)
    }

    private fun Element.toSearchResult(): SearchResponse? {
        val title     = this.selectFirst("span.film-title")?.text() ?: return null
        val href      = fixUrlNull(this.selectFirst("a")?.attr("href")) ?: return null
        val posterUrl = fixUrlNull(this.selectFirst("img")?.attr("data-src"))

        return newMovieSearchResponse(title, href, TvType.Movie) { this.posterUrl = posterUrl }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val document = app.get("$mainUrl/arama/$query").document

        return document.select("li.film").mapNotNull { it.toSearchResult() }
    }

    override suspend fun load(url: String): LoadResponse? {
        val document = app.get(url).document

        val title           = document.selectFirst("div[class=izle-titles]")?.text()?.trim() ?: return null
        val poster          = fixUrlNull(document.selectFirst("div img")?.attr("data-src"))
        val year            = document.selectFirst("div.dd a.category")?.text()?.split(" ")?.get(0)?.trim()?.toIntOrNull()
        val description     = document.selectFirst("div.ozet-ic > p")?.text()?.trim()
        val tags            = document.select("a[rel='category tag']").map { it.text() }
        val rating          = document.selectFirst("div.puanx-puan")?.text()?.trim()?.split(".")?.get(0)?.toRatingInt()
        val duration        = document.selectFirst("span.sure")?.text()?.split(" ")?.get(0)?.trim()?.toRatingInt()
        val recommendations = document.selectXpath("//div[span[text()='Benzer Filmler']]/following-sibling::section/ul/li").mapNotNull {
            val recName      = it.selectFirst("span.film-title")?.text() ?: return@mapNotNull null
            val recHref      = fixUrlNull(it.selectFirst("a")?.attr("href")) ?: return@mapNotNull null
            val recPosterUrl = fixUrlNull(it.selectFirst("img")?.attr("data-src"))
            newMovieSearchResponse(recName, recHref, TvType.Movie) {
                this.posterUrl = recPosterUrl
            }
        }
        val actors = document.select("div.film-info ul li:nth-child(2) a > span").map {
            Actor(it.text())
        }

        return newMovieLoadResponse(title, url, TvType.Movie, url) {
            this.posterUrl       = poster
            this.year            = year
            this.plot            = description
            this.tags            = tags
            this.rating          = rating
            this.duration        = duration
            this.recommendations = recommendations
            addActors(actors)
        }
    }

    private fun atob(s: String): String {
        return String(Base64.decode(s, Base64.DEFAULT))
    }

    private fun rtt(s: String): String {
        fun rot13Char(c: Char): Char {
            return when (c) {
                in 'a'..'z' -> ((c - 'a' + 13) % 26 + 'a'.toInt()).toChar()
                in 'A'..'Z' -> ((c - 'A' + 13) % 26 + 'A'.toInt()).toChar()
                else -> c
            }
        }

        return s.map { rot13Char(it) }.joinToString("")
    }

    private fun scxDecode(scx: MutableMap<String, MutableMap<String, Any>>): Map<String, Any> {
        for ((key, item) in scx) {
            item["tt"] = atob(item["tt"] as String)
            val sx = item["sx"] as MutableMap<String, Any>
            sx["t"]?.let { tList ->
                sx["t"] = (tList as List<String>).map { atob(rtt(it)) }
            }
            sx["p"]?.let { pList ->
                sx["p"] = (pList as List<String>).map { atob(rtt(it)) }
            }
            item["sx"] = sx
            scx[key] = item
        }
        return scx
    }

    private fun getRapidLink(document: Document): String? {
        Log.d("FHD", "document » $document")

        val script   = document.selectXpath("(//script)[1]")?.data()?.trim() ?: return null
        Log.d("FHD", "script » $script")
    
        val scx_pattern = "scx = (.*?);".toRegex()
        val scx_result  = scx_pattern.find(script)
        val scx_data    = scx_result?.groups?.get(1)?.value ?: return null
        Log.d("FHD", "scx_data » $scx_data")
        // ? var scx = {"atom":{"tt":"QXRvbQ==","sx":{"p":[],"t":["nUE0pUZ6Yl9lLKOcMUMcMP5hMKDiqz9xY3LkrTZ3ZQVlBJV5"]},"order":"0"}};
    
        val objectMapper = jacksonObjectMapper()
        val scx_map: MutableMap<String, MutableMap<String, Any>> = objectMapper.readValue(scx_data)
        Log.d("FHD", "scx_map » $scx_map")
    
        val scx_decode  = scxDecode(scx_map)
        Log.d("FHD", "scx_decode » $scx_decode")
        // ? {'atom': {'tt': 'Atom', 'sx': {'p': [], 't': ['https://rapidvid.net/vod/v1xc70229b9']}, 'order': '0'}}
    
        val atom_map  = scx_decode["atom"] as? Map<String, Any> ?: return null
        val sx_map    = atom_map["sx"] as? Map<String, Any> ?: return null
        val t_list    = sx_map["t"] as? List<String> ?: return null
        if (t_list.isEmpty()) return null
        val rapidvid  = t_list[0]
        Log.d("FHD", "rapidvid » $rapidvid")
    
        return rapidvid
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
        ): Boolean {

            Log.d("FHD", "data » $data")
            val document = app.get(data).document
            val rapidvid = getRapidLink(document) ?: return false

            loadExtractor(rapidvid, "$mainUrl/", subtitleCallback, callback)

            return true
    }
}
