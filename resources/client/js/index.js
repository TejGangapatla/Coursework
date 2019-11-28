function pageLoad() {

    let StudentHTML = `<table>` +
        '<tr>' +
        '<th>UserID</th>' +
        '<th>Name</th>' +
        '<th>Age</th>' +
        '<th>Email</th>' +
        '<th class="last">Settings</th>' +
        '</tr>';

    fetch('/Student/list', {method: 'get'}
    ).then(response => response.json()
    ).then(Students => {

        for (let Student of Students){

            StudentHTML += `<tr>` +
                `<td>${Student.UserID}</td>` +
                `<td>${Student.Name}</td>` +
                `<td>${Student.Age}</td>` +
                `<td>${Student.Email}</td>` +
                `<td class="last">` +
                `<button class='editButton' data-id='${Student.UserID}'>Edit</button>` +
                `</td>` +
                `</tr>`;


        }

        StudentHTML += '</table>';

        document.getElementById("listDiv").innerHTML = StudentHTML;
        let editButtons = document.getElementsByClassName("editButton");
        for (let button of editButtons) {
            button.addEventListener("click", editUser);
        }

    });

    //document.getElementById("saveButton").addEventListener("click", saveEditUser);
    //document.getElementById("cancelButton").addEventListener("click", cancelEditUser);

}

function editFruit(event) {

    const UserID = event.target.getAttribute("data-UserID");

    if (UserID === null) {

        document.getElementById("editHeading").innerHTML = 'Add new fruit:';

        document.getElementById("fruitId").value = '';
        document.getElementById("fruitName").value = '';
        document.getElementById("fruitImage").value = '';
        document.getElementById("fruitColour").value = '';
        document.getElementById("fruitSize").value = '';

        document.getElementById("listDiv").style.display = 'none';
        document.getElementById("editDiv").style.display = 'block';

    } else {

        fetch('/fruit/get/' + id, {method: 'get'}
        ).then(response => response.json()
        ).then(fruit => {

            if (fruit.hasOwnProperty('error')) {
                alert(fruit.error);
            } else {

                document.getElementById("editHeading").innerHTML = 'Editing ' + fruit.name + ':';

                document.getElementById("fruitId").value = id;
                document.getElementById("fruitName").value = fruit.name;
                document.getElementById("fruitImage").value = fruit.image;
                document.getElementById("fruitColour").value = fruit.colour;
                document.getElementById("fruitSize").value = fruit.size;

                document.getElementById("listDiv").style.display = 'none';
                document.getElementById("editDiv").style.display = 'block';

            }

        });

    }

}


