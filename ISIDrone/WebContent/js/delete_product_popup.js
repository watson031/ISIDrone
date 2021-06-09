$(function() {

  $("#dialog").dialog({
     autoOpen: false,
     modal: true
   });

  $("#myButton").on("click", function(e) {
      e.preventDefault();
      $("#dialog").dialog("open");
  });

});

//var row = document.getElementsByClassName("row")[0];
//var btn = document.getElementById("myButton");
//var modal = document.getElementById("dialog");
//
//btn.onclick = function() {
//  row.style.backgroundColor = "red";
//}