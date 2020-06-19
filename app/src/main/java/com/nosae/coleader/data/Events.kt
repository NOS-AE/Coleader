package com.nosae.coleader.data

import com.nosae.coleader.adapters.ChatItem

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

class ReceiveChatEvent(val msg: List<ChatItem>?)

class UpdatePunchEvent

class EditTaskEvent(val task: Task)

class UpdateTaskEvent

class TaskEvent(val task: Task)

class ReceiveUserMessageEvent(val msg: UserResultMessage)

class ReceiveTeamMessageEvent(val msg: TeamResultMessage)

class FormEvent(val form: Form)

class UpdateLastMessageEvent