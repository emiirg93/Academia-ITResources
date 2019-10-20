$(function () {
  loadData()
});

function updateView(data) {

  var cuerpoTabla = $("#bodyTabla");

  for (let i in data) {
    game = [
      data[i]["created"],
      data[i]["gamePlayers"]
    ];

    var fila = $("<tr>");
    data[i] = game;

    fechaArr = data[i][0].split("T");

    var columna = $("<td>").text(fechaArr[0]);
    var columna1 = $("<td>").text(fechaArr[1]);

    fila.append(columna);
    fila.append(columna1);
    
    for (var key in data[i][1]) {
      var columna2 = $("<td>").text(data[i][1][key]["player"]["email"]);
      fila.append(columna2);
    }
    
    cuerpoTabla.append(fila)
  }
}


  // load and display JSON sent by server for /players

  function loadData() {
    $.get("/api/games")
      .done(function (data) {
        updateView(data);
      })
      .fail(function (jqXHR, textStatus) {
        alert("Failed: " + textStatus);
      });
  }