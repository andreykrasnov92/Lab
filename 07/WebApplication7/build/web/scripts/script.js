
function start() {
    if (document.getElementById("timer") !== null) {
        timer();
    }
    else {
        setTimeout("start();", 10);
    }
}

start();

function timer() {
    try {
        s = document.getElementById("timer").textContent.toString();
        i = parseInt(s, 0);
        if (!isNaN(i)) {
            if (i > 0) {
                document.getElementById("timer").textContent = i - 1;
                setTimeout("timer();", 1000);
            }
        }
    } catch (exception) {
    }
}
