window.onload = function() {
    document.querySelectorAll(".btn-danger").forEach(v => v.onclick = () => confirm("Are you sure?"));
};