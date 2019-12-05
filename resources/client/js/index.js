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
                `<button class='editButton' data-UserID='${Student.UserID}'>Edit</button>` +
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

    document.getElementById("saveButton").addEventListener("click", saveEditUser);
    document.getElementById("cancelButton").addEventListener("click", cancelEditUser);

}

function editUser(event) {

    const UserID = event.target.getAttribute("data-UserID");

    fetch('/Student/list/' + UserID, {method: 'get'}
    ).then(response => response.json()
    ).then(Student => {

        if (Student.hasOwnProperty('error')) {
            alert(Student.error);
        } else {

            document.getElementById("editHeading").innerHTML = 'Editing ' + Student.Name + ':';

            document.getElementById("StudentUserID").value = UserID;
            document.getElementById("StudentName").value = Student.Name;
            document.getElementById("StudentAge").value = Student.Age;
            document.getElementById("StudentEmail").value = Student.Email;


            document.getElementById("listDiv").style.display = 'none';
            document.getElementById("editDiv").style.display = 'block';

        }

    });

}

function saveEditUser(event) {


    event.preventDefault();

    if (document.getElementById("StudentName").value.trim() === '') {
        alert("Please provide a Student Name.");
        return;
    }

    if (document.getElementById("StudentAge").value.trim() === '') {
        alert("Please provide a Student Age.");
        return;
    }

    if (document.getElementById("StudentEmail").value.trim() === '') {
        alert("Please provide a Student Email.");
        return;
    }



    const UserID = document.getElementById("StudentUserID").value;
    const form = document.getElementById("StudentForm");
    const formData = new FormData(form);

    let apiPath = '';
    if (UserID === '') {
        apiPath = '/Student/new';
    } else {
        apiPath = '/Student/update';
    }

    fetch(apiPath, {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {

        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
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




