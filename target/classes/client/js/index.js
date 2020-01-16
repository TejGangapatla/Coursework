function pageLoad() {

    let StudentHTML = `<table>` +
        '<tr>' +
        '<th>ID</th>' +
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
                `<td>${Student.id}</td>` +
                `<td>${Student.name}</td>` +
                `<td>${Student.age}</td>` +
                `<td>${Student.email}</td>` +
                `<td class="last">` +
                `<button class='editButton' data-id='${Student.id}'>Edit</button>` +
                `</td>` +
                `</tr>`;


        }

        StudentHTML += '</table>';

        document.getElementById("listDiv").innerHTML = StudentHTML;
        let editButtons = document.getElementsByClassName("editButton");
        for (let button of editButtons) {
            button.addEventListener("click", editUser);
        }

        checkLogin();

    });

    document.getElementById("saveButton").addEventListener("click", saveEditUser);
    document.getElementById("cancelButton").addEventListener("click", cancelEditUser);

}

function editUser(event) {

    const id = event.target.getAttribute("data-id");

    fetch('/Student/get/' + id, {method: 'get'}
    ).then(response => response.json()
    ).then(Student => {

        if (Student.hasOwnProperty('error')) {
            alert(Student.error);
        } else {

            document.getElementById("editHeading").innerHTML = 'Editing ' + Student.name + ':';

            document.getElementById("Studentid").value = id;
            document.getElementById("Studentname").value = Student.name;
            document.getElementById("Studentage").value = Student.age;
            document.getElementById("Studentemail").value = Student.email;


            document.getElementById("listDiv").style.display = 'none';
            document.getElementById("editDiv").style.display = 'block';

        }

    });

}

function saveEditUser(event) {


    event.preventDefault();

    if (document.getElementById("Studentname").value.trim() === '') {
        alert("Please provide a Student Name.");
        return;
    }

    if (document.getElementById("Studentage").value.trim() === '') {
        alert("Please provide a Student Age.");
        return;
    }

    if (document.getElementById("Studentemail").value.trim() === '') {
        alert("Please provide a Student Email.");
        return;
    }



    const id = document.getElementById("Studentid").value;
    const form = document.getElementById("StudentForm");
    const formData = new FormData(form);





    fetch('/Student/update', {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(Student => {

        if (Student.hasOwnProperty('error')) {
            alert(Student.error);
        } else {
            document.getElementById("listDiv").style.display = 'block';
            document.getElementById("editDiv").style.display = 'none';
            pageLoad();
        }
    });

}

function cancelEditUser(event) {

    event.preventDefault();

    document.getElementById("listDiv").style.display = 'block';
    document.getElementById("editDiv").style.display = 'none';

}

function checkLogin() {

    let id = Cookies.get("id");

    let logInHTML = '';

    if (id === undefined) {

        let editButtons = document.getElementsByClassName("editButton");
        for (let button of editButtons) {
            button.style.visibility = "hidden";
        }

        let deleteButtons = document.getElementsByClassName("deleteButton");
        for (let button of deleteButtons) {
            button.style.visibility = "hidden";
        }

        logInHTML = "Not logged in. <a href='/client/login.html'>Log in</a>";
    } else {

        let editButtons = document.getElementsByClassName("editButton");
        for (let button of editButtons) {
            button.style.visibility = "visible";
        }

        let deleteButtons = document.getElementsByClassName("deleteButton");
        for (let button of deleteButtons) {
            button.style.visibility = "visible";
        }

        logInHTML = "Logged in as " + id + ". <a href='/client/login.html?logout'>Log out</a>";

    }

    document.getElementById("loggedInDetails").innerHTML = logInHTML;

}








