SELECT name FROM users JOIN messages
ON users.id=messages.user_id
GROUP BY user_id HAVING count(user_id) > 3;
