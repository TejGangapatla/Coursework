function pageLoad() {

    let StudentHTML = `<table>` +
        '<tr>' +
        '<th>UserID</th>' +
        '<th>Name</th>' +
        '<th>Age</th>' +
        '<th class="last">Email</th>' +
        '</tr>';




    document.getElementById("testDiv").innerHTML = StudentHTML;

}

