package com.dinesh.basketball.models

data class TeamsListModel(
    val `data`: TeamsData
)

data class TeamsData(
    val teams: List<Team>
)

data class Team(
    val co: String,
    val color: String,
    val custom_fields: Any,
    val di: String,
    val ist_group: String,
    val league_id: String,
    val logo: String,
    val season_id: String,
    val sta: String,
    val ta: String,
    val tc: String,
    val template_fields: TemplateFields,
    val tid: String,
    val tn: String,
    val uid: String,
    val year: Int
)

data class TemplateFields(
    val ACL: ACL,
    val _version: Int,
    val created_at: String,
    val created_by: String,
    val dfe_class_uid: String,
    val league_id: String,
    val locale: String,
    val logo: String,
    val publish_details: PublishDetails,
    val season_id: String,
    val ta: String,
    val tags: List<Any>,
    val tc: String,
    val tid: String,
    val title: String,
    val tn: String,
    val updated_at: String,
    val updated_by: String,
    val year: Int
)

data class PublishDetails(
    val environment: String,
    val locale: String,
    val time: String,
    val user: String
)

class ACL