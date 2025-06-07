package com.loki.utils.network.bean.search

/**
 * {
 *   "result": {
 *     "songs": [
 *       {
 *         "id": 1357375695,
 *         "name": "海阔天空",
 *         "artists": [
 *           {
 *             "id": 11127,
 *             "name": "Beyond",
 *             "picUrl": null,
 *             "alias": [],
 *             "albumSize": 0,
 *             "picId": 0,
 *             "fansGroup": null,
 *             "img1v1Url": "https://p3.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg",
 *             "img1v1": 0,
 *             "trans": null
 *           }
 *         ],
 *         "album": {
 *           "id": 78372827,
 *           "name": "华纳廿三周年纪念精选系列 - Beyond",
 *           "artist": {
 *             "id": 0,
 *             "name": "",
 *             "picUrl": null,
 *             "alias": [],
 *             "albumSize": 0,
 *             "picId": 0,
 *             "fansGroup": null,
 *             "img1v1Url": "https://p4.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg",
 *             "img1v1": 0,
 *             "trans": null
 *           },
 *           "publishTime": 999273600000,
 *           "size": 15,
 *           "copyrightId": 7002,
 *           "status": 1,
 *           "picId": 109951163984013010,
 *           "mark": 0
 *         },
 *         "duration": 239560,
 *         "copyrightId": 7002,
 *         "status": 0,
 *         "alias": [],
 *         "rtype": 0,
 *         "ftype": 0,
 *         "mvid": 5501497,
 *         "fee": 1,
 *         "rUrl": null,
 *         "mark": 17179877376
 *       }
 *     ],
 *     "hasMore": true,
 *     "songCount": 328
 *   },
 *   "code": 200
 * }
 */
data class SearchResponse(
    val code: Int?,
    val result: Result?
)

data class Result(
    val hasMore: Boolean?,
    val songCount: Int?,
    val songs: List<Song>?
)

data class Album(
    val alia: List<String>?,
    val artist: ArtistX?,
    val copyrightId: Int?,
    val id: Int?,
    val mark: Int?,
    val name: String?,
    val picId: Long?,
    val publishTime: Long?,
    val size: Int?,
    val status: Int?,
    val transNames: List<String>?
)

data class ArtistX(
    val albumSize: Int?,
    val alias: List<Any?>?,
    val fansGroup: Any?,
    val id: Int?,
    val img1v1: Int?,
    val img1v1Url: String?,
    val name: String?,
    val picId: Int?,
    val picUrl: Any?,
    val trans: Any?
)

data class Song(
    val album: Album?,
    val alias: List<String>?,
    val artists: List<ArtistX>?,
    val copyrightId: Int?,
    val duration: Int?,
    val fee: Int?,
    val ftype: Int?,
    val id: Long?,
    val mark: Long?,
    val mvid: Int?,
    val name: String?,
    val rUrl: Any?,
    val rtype: Int?,
    val status: Int?,
    val transNames: List<String>?
)