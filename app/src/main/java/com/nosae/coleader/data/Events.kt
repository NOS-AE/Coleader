package com.nosae.coleader.data

/**
 * Create by NOSAE on 2020/5/8
 */

class BadgeEvent(val number: Int)

class CreateTeamEvent(val newTeam: Team)

class ShowTeamEvent(val team: Team)

class EditDateEvent(val date: Date)

class UpdateDateEvent

class PunchEvent(val punch: Punch)

class PunchFinishEvent

class ReceivePunchEvent(val punches: List<Punch>?)