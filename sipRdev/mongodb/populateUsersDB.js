db = new Mongo().getDB("sipr")
// load users
for (i = 10000; i < 20001; i++) {
    db.users.insert( { userName: i + "", sipPassword: "1234" } )
}
// test user
db.users.insert( { userName: "200", sipPassword: "e2e047fd0adc3246d8998c8f3f021328" } )
// add index on userName
db.users.createIndex( { userName: 1 } )