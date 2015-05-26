select text from messages join users
where users.id=messages.user_id and users.name = 'dima';