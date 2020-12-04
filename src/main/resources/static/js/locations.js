var state = "default";

window.onload = function () {
    let locationForm = document.getElementById("location-form");
    locationForm.onsubmit = createLocation;

    let updateLocationForm = document.getElementById("update-location-form");
    updateLocationForm.onsubmit = updateLocation;

    document.querySelector("#delete-location-form").onsubmit = deleteLocation;

    document.getElementById("update-cancel-button").onclick = cancelUpdate;

    document.getElementById("create-cancel-button").onclick = cancelCreate;

    document.getElementById("delete-cancel-button").onclick = cancelDelete;

    document.getElementById("create-location-link").onclick = prepareCreateLocation;

    document.querySelector("#refresh-button").onclick = downloadLocations;

    document.querySelector("#load-button").onclick = load;

    document.querySelector("#save-as-button").onclick = save;

    initScreen();
};

async function initScreen() {
    if (service.refreshEnabled()) {
        document.querySelector("#refresh-button").removeAttribute("hidden");
    }
    if (service.exportEnabled()) {
        document.querySelector("#export-buttons").removeAttribute("hidden");
    }    
    downloadLocations();
}

function setState(pState) {
    state = pState;
    hideMessage();
    if (state == "default") {
        document.getElementById("create-location-link").removeAttribute("hidden");
        document.getElementById("location-form").setAttribute("hidden", "hidden");
        document.getElementById("update-location-form").setAttribute("hidden", "hidden");
        document.getElementById("delete-location-form").setAttribute("hidden", "hidden");
    }
    if (state == "create") {
        document.getElementById("location-name").value = "";
        toggleValid("location-name", true);
        document.getElementById("location-coords").value = "";
        toggleValid("location-coords", true);
        document.getElementById("location-interesting-at").value = "";
        toggleValid("location-interesting-at", true);
        document.getElementById("location-tags").value = "";

        document.getElementById("create-location-link").setAttribute("hidden", "hidden");
        document.getElementById("location-form").removeAttribute("hidden");
        document.getElementById("update-location-form").setAttribute("hidden", "hidden");
        document.getElementById("delete-location-form").setAttribute("hidden", "hidden");
    }
    if (state == "update") {
        toggleValid("update-location-name", true);
        toggleValid("update-location-coords", true);
        toggleValid("update-location-interesting-at", true);

        document.getElementById("create-location-link").setAttribute("hidden", "hidden");
        document.getElementById("location-form").setAttribute("hidden", "hidden");
        document.getElementById("update-location-form").removeAttribute("hidden");
        document.getElementById("delete-location-form").setAttribute("hidden", "hidden");
    }
    if (state == "delete") {
        document.getElementById("create-location-link").setAttribute("hidden", "hidden");
        document.getElementById("location-form").setAttribute("hidden", "hidden");
        document.getElementById("update-location-form").setAttribute("hidden", "hidden");
        document.getElementById("delete-location-form").removeAttribute("hidden");
    }
}

function prepareCreateLocation() {
    setState("create");
    return false;
}

function cancelUpdate() {
    setState("default");
}

function cancelCreate() {
    document.getElementById("location-form").setAttribute("hidden", "hidden");
    document.getElementById("create-location-link").removeAttribute("hidden");
    setState("default");
}

function cancelDelete() {
    setState("default");
}

function hideMessage() {
    document.getElementById("message-div").innerHTML = "";
    document.getElementById("message-div").removeAttribute("class");
}

function downloadLocations() {
    service.findAll(fillTable);
}

function fillTable(locations) {
    var locationsTable = document.getElementById("locations-table-tbody");
    locationsTable.innerHTML = "";
    for (let location of locations) {
        var tr = document.createElement("tr");
        tr["raw-data"] = location;

        var idTd = document.createElement("td");
        idTd.innerHTML = location.id;
        tr.appendChild(idTd);

        var nameTd = document.createElement("td");
        nameTd.innerHTML = location.name;
        tr.appendChild(nameTd);

        var coordsTd = document.createElement("td");
        coordsTd.innerHTML = location.lat + ", " + location.lon;
        tr.appendChild(coordsTd);

        var interestingAtTd = document.createElement("td");
        if (location.interestingAt) {
            interestingAtTd.innerHTML = location.interestingAt;
        }
        tr.appendChild(interestingAtTd);

        var tagsTd = document.createElement("td");
        if (location.tags) {
            tagsTd.innerHTML = location.tags;
        }
        tr.appendChild(tagsTd);

        var buttonsTd = document.createElement("td");
        var editButton = document.createElement("button");
        editButton.setAttribute("class", "btn btn-link");
        editButton.innerHTML = "Edit";
        editButton.onclick = prepareForUpdateLocation;
        buttonsTd.appendChild(editButton);

        var deleteButton = document.createElement("button");
        deleteButton.setAttribute("class", "btn btn-danger");
        deleteButton.innerHTML = "Delete";
        deleteButton.onclick = prepareDeleteLocation;
        buttonsTd.appendChild(deleteButton);

        tr.appendChild(buttonsTd);

        locationsTable.appendChild(tr);

    }
}

function prepareForUpdateLocation() {
    setState("update");
    window.scrollTo(0, 0);

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

function toggleValid(componentName, isValid, message) {
    if (isValid) {
        document.getElementById(componentName).classList.remove("is-invalid");
        document.getElementById(componentName + "-feedback").setAttribute("hidden", "hidden");
    }
    else {
        document.getElementById(componentName).classList.add("is-invalid");
        document.getElementById(componentName + "-feedback").removeAttribute("hidden");
        document.getElementById(componentName + "-feedback").innerHTML = message;
    }
}

function validateForm(nameInput, coordsInput, interestingAtInput) {
    let valid = true;

    const name = document.getElementById(nameInput).value;
    if (/^$|^\s+$/.test(name)) {
        toggleValid(nameInput, false, "Can not be empty name!");
        valid = false;
    }
    else {
        toggleValid(nameInput, true);
    }

    const coords = document.getElementById(coordsInput).value;
    if (!/^-?\d+(\.\d{1,7})?\,-?\d+(\.\d{1,7})?$/.test(coords)) {
        toggleValid(coordsInput, false, "Invalid format!");
        valid = false;
    }
    else {
        const parts = coords.split(",");
        const lat = parseFloat(parts[0]);
        const lon = parseFloat(parts[1]);
        if (lat < -90 || lat > 90) {
            toggleValid(coordsInput, false, "Latitude must be between -90 and 90");
            valid = false;
        }
        else if (lon < -180 || lon > 180) {
            toggleValid(coordsInput, false, "Longitude must be between -180 and 180");
            valid = false;
        }
        else {
            toggleValid(coordsInput, true);
        }
    }

    const interestingAt = document.getElementById(interestingAtInput).value;
    if (!("" == interestingAt) && !/^\d{4}\-\d\d-\d\dT\d\d:\d\d:\d\d$/.test(interestingAt)) {
        toggleValid(interestingAtInput, false, "Invalid format!");
        valid = false;
    }
    else {
        toggleValid(interestingAtInput, true);
    }

    return valid;
}

function createLocation() {
    const valid = validateForm("location-name", "location-coords", "location-interesting-at");

    if (!valid) {
        return false;
    }

    let name = document.getElementById("location-name").value;
    let coords = document.getElementById("location-coords").value;
    let interestingAt = document.getElementById("location-interesting-at").value;
    let tags = document.getElementById("location-tags").value;

    let request = { "name": name, "coords": coords, "interestingAt": interestingAt, "tags": tags };

    service.save(request, handleCreateResponse);

    return false;
}

function handleCreateResponse(jsonData) {
    if (jsonData.status === 201) {
        successCreate();
    }
    else {
        printMessage(getErrorMessages(jsonData), "alert-danger");
    }
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
    printMessage("Location has been created", "alert-success");
}

function prepareDeleteLocation() {    
    setState("delete");
    window.scrollTo(0, 0);
    var location = this.parentElement.parentElement["raw-data"];
    document.querySelector("#location-id-hidden").value = location.id;
}

function deleteLocation() {
    var id = document.querySelector("#location-id-hidden").value;    
    service.deleteById(id, successDelete);
    return false;
}

function successDelete() {
    console.log("Successfull delete");
    setState("default");
    downloadLocations();
    printMessage("Location has been deleted", "alert-success");
}

function updateLocation() {
    const valid = validateForm("update-location-name", "update-location-coords", "update-location-interesting-at");

    if (!valid) {
        return false;
    }

    let id = document.getElementById("update-location-id").value;
    let name = document.getElementById("update-location-name").value;
    let coords = document.getElementById("update-location-coords").value;
    let interestingAt = document.getElementById("update-location-interesting-at").value;
    let tags = document.getElementById("update-location-tags").value;

    let request = { "name": name, "coords": coords, "interestingAt": interestingAt, "tags": tags };

    service.update(id, request, handleUpdateResponse);

    return false;
}

function handleUpdateResponse(jsonData) {
    if (jsonData.status == 200) {
        successUpdate();
    }
    else {
        printMessage(getErrorMessages(jsonData), "alert-danger");
    }
}

function successUpdate() {
    console.log("Successfull update");
    document.getElementById("update-location-id").value = "";
    document.getElementById("update-location-name").value = "";
    document.getElementById("update-location-coords").value = "";
    downloadLocations();
    setState("default");
    printMessage("Location has been modified", "alert-success");
}

function printMessage(message, styleClass) {
    document.getElementById("message-div").innerHTML = message;
    document.getElementById("message-div").setAttribute("class", "alert " + styleClass);
}

function load() {
    dialog.showOpenDialog({
        properties: ['openFile']
    }).then(function (files) {
        if (files !== undefined && files.filePaths.length > 0) {
            try {
                fs.readFile(files.filePaths[0], (err, data) => {
                    if (err) throw err;
                    const locations = JSON.parse(data);
                    service.populate(locations, () => { 
                        downloadLocations(); 
                        printMessage("The file has been successfully loaded! Number of locations: " + locations.length, "alert-success");
                    });
                });                
            }
            catch (e) {
                printMessage("Can not read file: " + e, "alert-danger");
            }
        }
    });
}

function save() {
    dialog.showSaveDialog({
        properties: ["showOverwriteConfirmation"]
    }).then(function (files) {
        if (files !== undefined && !files.canceled) {
            locations = service.findAll(function (jsonData) {
                let data = JSON.stringify(jsonData);
                fs.writeFile(files.filePath, data, (err) => {
                    if (err) {
                        printMessage("Can not save file: " + e, "alert-danger");
                    }
                    else {
                        printMessage("The file has been successfully saved.", "alert-success");
                    }

                });
            });

        }
    });
}