function selectOnlyOne(id) {
    for (var i = 1;i <= 4; i++)
    {
        document.getElementById("Check" + i).checked = false;
    }
    document.getElementById(id).checked = true;
}