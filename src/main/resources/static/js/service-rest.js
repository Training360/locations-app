let prefix = ''

function findAll(callback) {
    let url = prefix + 'api/locations';
    fetch(url)
            .then(function(response) {
                return response.json();
                })
            .then(function(jsonData) {
                callback(jsonData);
            });
}

function save(data, callback) {
    const url = prefix + 'api/locations';
    const jsonData = JSON.stringify(data)

    fetch("api/locations", {
            method: "POST",
            body: jsonData,
            headers: {
                "Content-Type": "application/json"
            }
        })
            .then(function(response) {
                return response.json().then(function(jsonData) {
                    callback({status: response.status, body: jsonData});
                });
            });


}


function deleteById(id, callback) {
    const url = prefix + 'api/locations/' + id;
    fetch(url, {
                method: "DELETE"
            })
            .then(function(response) {
                callback();
            })
}

function update(id, data, callback) {
    const url = prefix + 'api/locations/' + id;
    const jsonData = JSON.stringify(data)
    fetch(url, {
            method: "POST",
            body: jsonData,
            headers: {
                "Content-Type": "application/json"
            }
        })
        .then(function(response) {
            return response.json().then(function(jsonData) {
                callback({status: response.status, body: jsonData});
            });
        })
}

function refreshEnabled() {
    return true;
}

function exportEnabled() {
    return false;
}

const service = {
    findAll: findAll,
    save: save,
    deleteById: deleteById,
    update: update,
    refreshEnabled: refreshEnabled,
    exportEnabled: exportEnabled
}