var state = "default";

window.onload = function() {
    initScreen();
    let locationForm = document.getElementById("location-form");
    locationForm.onsubmit = createLocation;

    let updateLocationForm = document.getElementById("update-location-form");
    updateLocationForm.onsubmit = updateLocation;

    let prevLink = document.getElementById("prev-link");
    prevLink.onclick = doPaging;
    let nextLink = document.getElementById("next-link");
    nextLink.onclick = doPaging;

    document.getElementById("update-cancel-button").onclick = cancelUpdate;

    document.getElementById("create-cancel-button").onclick = cancelCreate;

    document.getElementById("create-location-link").onclick = prepareCreateLocation;

    updateWebservicesLink();
};

function initScreen() {
    let page = getParameterByName("page");
    if (page == null) {
        page = 0;
    }
    let size = getParameterByName("size");
    if (size == null) {
        size = 10;
    }
    downloadLocations(page, size);
}

function updateWebservicesLink() {
    let url = window.location.href;
    let base = url.substring(0, url.lastIndexOf("/"));
    let address = base + "/services";
    let link = document.getElementById("webservices-link");
    link.href = address;
    link.innerHTML = address;
}

function setState(pState) {
    state = pState;
    if (state == "default") {
        document.getElementById("location-name").value = "";
        document.getElementById("location-coords").value = "";
        document.getElementById("location-interesting-at").value = "";
        document.getElementById("location-tags").value = "";
        document.getElementById("create-location-link").removeAttribute("hidden");
        document.getElementById("location-form").setAttribute("hidden", "hidden");
        document.getElementById("update-location-form").setAttribute("hidden", "hidden");
    }
    if (state == "create") {
        document.getElementById("create-location-link").setAttribute("hidden", "hidden");
        document.getElementById("location-form").removeAttribute("hidden");
        document.getElementById("update-location-form").setAttribute("hidden", "hidden");
    }
    if (state == "update") {
            document.getElementById("create-location-link").setAttribute("hidden", "hidden");
            document.getElementById("location-form").setAttribute("hidden", "hidden");
            document.getElementById("update-location-form").removeAttribute("hidden");
    }
}

function prepareCreateLocation() {
    setState("create");
    return false;
}

function cancelUpdate() {
    document.getElementById("message-div").innerHTML = "";
    document.getElementById("message-div").removeAttribute("class");
    setState("default");
}

function cancelCreate() {
    document.getElementById("message-div").innerHTML = "";
    document.getElementById("message-div").removeAttribute("class");
    document.getElementById("location-form").setAttribute("hidden", "hidden");
    document.getElementById("create-location-link").removeAttribute("hidden");
}

function doPaging() {
    let link = this.getAttribute("href");
    let page = getParameterByName("page", link);
    let size = getParameterByName("size", link);
    downloadLocations(page, size);
    window.history.pushState("Locations", "Locations (page " + page + ")", "?page=" + page + "&size=" + size);
    return false;
}

function downloadLocations(page, size) {
    let url = 'api/locations?page=' + page + "&size=" + size;

    fetch(url)
        .then(function(response) {
            return response.json();
            })
        .then(function(jsonData) {
            initPagination(jsonData.number, jsonData.totalPages, size);
            fillTable(jsonData.content);
        });
}

function fillTable(locations) {
  var locationsTable = document.getElementById("locations-table-tbody");
  locationsTable.innerHTML = "";
  for (var i = 0; i < locations.length; i++) {
    var tr = document.createElement("tr");
    tr["raw-data"] = locations[i];

    var idTd = document.createElement("td");
    idTd.innerHTML = locations[i].id;
    tr.appendChild(idTd);

    var nameTd = document.createElement("td");
    nameTd.innerHTML = locations[i].name;
    tr.appendChild(nameTd);

    var coordsTd = document.createElement("td");
    coordsTd.innerHTML = locations[i].lat + ", " + locations[i].lon;
    tr.appendChild(coordsTd);

    var interestingAtTd = document.createElement("td");
    if (locations[i].interestingAt) {
        interestingAtTd.innerHTML = locations[i].interestingAt;
    }
    tr.appendChild(interestingAtTd);

    var tagsTd = document.createElement("td");
    tagsTd.innerHTML = locations[i].tags;
    tr.appendChild(tagsTd);

    var buttonsTd = document.createElement("td");
    var editButton = document.createElement("button");
    editButton.setAttribute("class", "btn btn-link");
    editButton.innerHTML= "Edit";
    editButton.onclick = prepareForUpdateLocation;
    buttonsTd.appendChild(editButton);

    var deleteButton = document.createElement("button");
    deleteButton.setAttribute("class", "btn btn-danger");
    deleteButton.innerHTML= "Delete";
    deleteButton.onclick = deleteLocation;
    buttonsTd.appendChild(deleteButton);

    tr.appendChild(buttonsTd);

    locationsTable.appendChild(tr);
  }
}

function initPagination(page, totalPages, size) {
    if (page === 0) {
        document.getElementById("prev-link").setAttribute("hidden", "hidden");
    }
    else {
        document.getElementById("prev-link").removeAttribute("hidden");
        document.getElementById("prev-link").setAttribute("href", "?page=" + (page - 1) + "&size=" + size)
    }
    document.getElementById("page-span").innerHTML = page;
    if (page < totalPages - 1) {
            document.getElementById("next-link").removeAttribute("hidden");
            document.getElementById("next-link").setAttribute("href", "?page=" + (page + 1) + "&size=" + size)
        }
        else {
            document.getElementById("next-link").setAttribute("hidden", "hidden");
        }
}

function prepareForUpdateLocation() {
    setState("update");

    var location = this.parentElement.parentElement["raw-data"];
    document.getElementById("update-location-id").value = location.id;
    document.getElementById("update-location-name").value = location.name;
    document.getElementById("update-location-coords").value = location.lat + "," + location.lon;
    if (location.interestingAt) {
        document.getElementById("update-location-interesting-at").value = location.interestingAt;
    }
    else {
        document.getElementById("update-location-interesting-at").value = "";
    }
    document.getElementById("update-location-tags").value = location.tags;
}

function createLocation() {
    let delay = getParameterByName("delay");

    if (!delay) {
        createLocationNow();
    }
    else {
        setTimeout(createLocationNow, delay);
    }
    return false;
}

function createLocationNow() {
    let name = document.getElementById("location-name").value;
    let coords = document.getElementById("location-coords").value;
    let interestingAt = document.getElementById("location-interesting-at").value;
    let tags = document.getElementById("location-tags").value;

    let request = {"name": name, "coords": coords, "interestingAt": interestingAt, "tags": tags};


    fetch("api/locations", {
        method: "POST",
        body: JSON.stringify(request),
        headers: {
            "Content-type": "application/json"
        }
    })
        .then(function(response) {
            return response.json().then(function(jsonData) {
                return {status: response.status, body: jsonData}
            });
        })
        .then(function(jsonData) {
            if (jsonData.status === 201) {
                successCreate();
            }
            else {
                document.getElementById("message-div").innerHTML = getErrorMessages(jsonData);
                document.getElementById("message-div").setAttribute("class", "alert alert-danger");
            }
        });
}

function getErrorMessages(jsonData) {
    let errorMessages = jsonData.body.validationErrors;
    let concatenated = "";
    errorMessages.forEach(errorMessage => concatenated += errorMessage.message + "; ");
    return concatenated;
}

function successCreate() {
    console.log("Successfull post");
    document.getElementById("location-name").value = "";
    document.getElementById("location-coords").value = "";
    downloadLocations();
    setState("default");
    document.getElementById("message-div").innerHTML = "Location has been created";
    document.getElementById("message-div").setAttribute("class", "alert alert-success");
}

function deleteLocation() {
    setState("default");
    if (!confirm("Are you sure to delete?")) {
        return;
    }
    var location = this.parentElement.parentElement["raw-data"];
    var url = 'api/locations/' + location.id;

    fetch(url, {
            method: "DELETE"
        })
        .then(function(response) {
            successDelete();
        })
        return false;
}

function successDelete() {
    console.log("Successfull delete");
    downloadLocations();
    document.getElementById("message-div").innerHTML = "Location has been deleted";
    document.getElementById("message-div").setAttribute("class", "alert alert-success");
}

function updateLocation() {
    let id = document.getElementById("update-location-id").value;
    let name = document.getElementById("update-location-name").value;
    let coords = document.getElementById("update-location-coords").value;
    let interestingAt = document.getElementById("update-location-interesting-at").value;
    let tags = document.getElementById("update-location-tags").value;

    let request = {"name": name, "coords": coords, "interestingAt": interestingAt, "tags": tags};

    let url = 'api/locations/' + id;

    fetch(url, {
        method: "POST",
        body: JSON.stringify(request),
        headers: {
            "Content-type": "application/json"
        }
    })
    .then(function(response) {
        return response.json().then(function(jsonData) {
            return {status: response.status, body: jsonData}
        });
    })
    .then(function(jsonData) {
        if (jsonData.status == 200) {
           successUpdate();
        }
        else {
            document.getElementById("message-div").innerHTML = getErrorMessages(jsonData);
            document.getElementById("message-div").setAttribute("class", "alert alert-danger");
        }
    });

    return false;
}

function successUpdate() {
    console.log("Successfull update");
    document.getElementById("update-location-id").value = "";
    document.getElementById("update-location-name").value = "";
    document.getElementById("update-location-coords").value = "";
    var page = document.getElementById("page-span").innerHTML;
    downloadLocations(page);
    setState("default");
    document.getElementById("message-div").innerHTML = "Location has been modified";
    document.getElementById("message-div").setAttribute("class", "alert alert-success");
}

function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, '\\$&');
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}