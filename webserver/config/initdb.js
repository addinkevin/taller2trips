User = require('../models/users');

module.exports = function(username, password) {
    User.findOne({ 'username' :  username }, function(err, user) {
        if (err)
            throw err;
        if (!user) {
            var newUser = new User();
            newUser.username = username;
            newUser.password = password;

            newUser.save(function(err) {
                if (err)
                    throw err;
            });
        }
    });
};