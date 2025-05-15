package com.dinesh.basketball.models

data class ScheduleListModel(
    val `data`: ScheduleData
)

data class ScheduleData(
    val schedules: List<Schedule>
)

data class Schedule(
    val arena_city: String,
    val arena_name: String,
    val arena_state: String,
    val bd: Bd,
    val buy_ticket: String,
    val buy_ticket_url: String,
    val cl: String,
    val cs_custom_fields: CsCustomFields,
    val custom_fields: Any,
    val game_state: String,
    val game_subtype: String,
    val gametime: String,
    val gcode: String,
    val gid: String,
    val h: H,
    val hide: Boolean,
    val is_game_necessary: String,
    val league_id: String,
    val logo_url: Any,
    val ppdst: String,
    val season_id: String,
    val seri: String,
    val st: Int,
    val stt: String,
    val template_fields: ScheduleTemplateFields,
    val uid: String,
    val v: V,
    val year: Int
)


data class V(
    val ist_group: String,
    val re: String,
    val s: String,
    val ta: String,
    val tc: String,
    val tid: String,
    val tn: String
)

data class Bd(
    val b: List<B>
)

data class B(
    val broadcasterId: Int,
    val custom_field_1: Any,
    val custom_field_2: Any,
    val dfe_broadcaster_logo: Any,
    val dfe_click_through_url: Any,
    val dfe_display_text: Any,
    val disp: String,
    val lan: String,
    val scope: String,
    val seq: Int,
    val type: String,
    val url: String
)


data class CsCustomFields(
    val family_night: String,
    val hide_tune_in: Boolean,
    val sponsor: Sponsor,
    val tune_in: List<Any>,
    val utility_menu: List<Any>
)

data class Sponsor(
    val clickthrough_link: String,
    val image: Any
)

data class H(
    val ist_group: String,
    val re: String,
    val s: String,
    val ta: String,
    val tc: String,
    val tid: String,
    val tn: String
)

data class ScheduleTemplateFields(
    val access_pass_setup: AccessPassSetup,
    val heat_jersey_link: String
)

data class AccessPassSetup(
    val available_passes: Int,
    val fortress_passes_sold: Any,
    val no_threshold: Boolean,
    val non_stm_cost: Int,
    val stm_cost: Int,
    val total_passes_allowed: Int
)