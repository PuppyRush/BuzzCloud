
    function saveText() {
        info = document.getElementById("info");
        info.style.display = "inline";
        localStorage.setItem("memo", msg.value);
        setTimeout(hide,3000);
    };
    function pageload() {
        msg = document.getElementById("txtBox");
        msg.value = localStorage.getItem("memo");
    };
    function clr() {
        msg.value = "";
        localStorage.clear();
        info.style.display = "none";
    };
    function hide()
    {
    	info = document.getElementById("info");
        info.style.display = "none";
    }