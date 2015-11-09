db = new Mongo().getDB("sipr")
// load users
for (i = 10000; i < 20001; i++) {
    db.users.insert( { userName: i + "", sipPassword: "1234" } )
}
// test user
db.users.insert( { userName: "200", sipPassword: "12345" } )
// add index on userName
db.users.createIndex( { userName: 1 } )