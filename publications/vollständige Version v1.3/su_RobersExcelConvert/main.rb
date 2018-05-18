require 'inifile'
require_relative "classes/ConnectionLoader"

class Main
  #er = ExcelReader.new
  #elements = er.loadDocument("real_test2.xlsm")


  # elements = [{"Lfd" => "1", "Sage Art." => "10001317", "Bezeichnung" => "DE", "Bauteil" => "De Fläche", "Materialgruppe" => "PL", "Werkstoff" => "OSB3 Platten 12mm", "Anz." => "1", "Länge" => "1270", "Breite" => "1170", "Höhe" => "12"},
  #             {"Lfd" => "2", "Sage Art." => "10001317", "Bezeichnung" => "SE", "Bauteil" => "Se Flächen", "Materialgruppe" => "PL", "Werkstoff" => "OSB3 Platten 12mm", "Anz." => "2", "Länge" => "619", "Breite" => "1270", "Höhe" => "12"},
  #             {"Lfd" => "3", "Sage Art." => "10001317", "Bezeichnung" => "KO", "Bauteil" => "Ko Flächen", "Materialgruppe" => "PL", "Werkstoff" => "OSB3 Platten 12mm", "Anz." => "2", "Länge" => "709", "Breite" => "1146", "Höhe" => "12"},
  #             {"Lfd" => "4", "Sage Art." => "10001596", "Bezeichnung" => "KO", "Bauteil" => "KoSp -lan waagrecht", "Materialgruppe" => "HO", "Werkstoff" => "Bretter, IPPC-HT-KD", "Anz." => "4", "Länge" => "1146", "Breite" => "100", "Höhe" => "23"},
  #             {"Lfd" => "5", "Sage Art." => "10001596", "Bezeichnung" => "KO", "Bauteil" => "KoSp !quer senkrecht", "Materialgruppe" => "HO", "Werkstoff" => "Bretter, IPPC-HT-KD", "Anz." => "6", "Länge" => "350", "Breite" => "100", "Höhe" => "23"},
  #             {"Lfd" => "6", "Sage Art." => "10001596", "Bezeichnung" => "SE", "Bauteil" => "SeSp -lan waagrecht", "Materialgruppe" => "HO", "Werkstoff" => "Bretter, IPPC-HT-KD", "Anz." => "4", "Länge" => "1200", "Breite" => "100", "Höhe" => "23"},
  #             {"Lfd" => "7", "Sage Art." => "10001596", "Bezeichnung" => "SE", "Bauteil" => "SeSp !aufr. Senkrecht", "Materialgruppe" => "HO", "Werkstoff" => "Bretter, IPPC-HT-KD", "Anz." => "8", "Länge" => "373", "Breite" => "100", "Höhe" => "23"},
  #             {"Lfd" => "8", "Sage Art." => "10001596", "Bezeichnung" => "DE", "Bauteil" => "DeSp längs durchlaufend", "Materialgruppe" => "HO", "Werkstoff" => "Bretter, IPPC-HT-KD", "Anz." => "3", "Länge" => "1246", "Breite" => "100", "Höhe" => "23"},
  #             {"Lfd" => "9", "Sage Art." => "10001596", "Bezeichnung" => "DE", "Bauteil" => "DeSp Füllstücke !quer", "Materialgruppe" => "HO", "Werkstoff" => "Bretter, IPPC-HT-KD", "Anz." => "8", "Länge" => "423", "Breite" => "100", "Höhe" => "23"},
  #             {"Lfd" => "10", "Sage Art." => "10001596", "Bezeichnung" => "DE", "Bauteil" => "DeSp Füllstücke -lang", "Materialgruppe" => "HO", "Werkstoff" => "Bretter, IPPC-HT-KD", "Anz." => "9", "Länge" => "282", "Breite" => "100", "Höhe" => "23"},
  #             {"Lfd" => "11", "Sage Art." => "10001596", "Bezeichnung" => "DE", "Bauteil" => "DeSp !quer durchlaufend", "Materialgruppe" => "HO", "Werkstoff" => "Bretter, IPPC-HT-KD", "Anz." => "4", "Länge" => "1146", "Breite" => "100", "Höhe" => "23"},
  #             {"Lfd" => "12", "Sage Art." => "10001384", "Bezeichnung" => "BO", "Bauteil" => "Bo Querkanthölzer", "Materialgruppe" => "HO", "Werkstoff" => "Kanthölzer, IPPC-HT-KD", "Anz." => "3", "Länge" => "1170", "Breite" => "80", "Höhe" => "100"},
  #             {"Lfd" => "13", "Sage Art." => "10001596", "Bezeichnung" => "BO", "Bauteil" => "Bo Lagen -lang", "Materialgruppe" => "HO", "Werkstoff" => "Bretter, IPPC-HT-KD", "Anz." => "1", "Länge" => "1246", "Breite" => "1100", "Höhe" => "23"}]
  #
  # ei = ElementIdentifier.new
  # entities = ei.identifyElements(elements)

  #puts ei.identifyElement(elements[0])

  #for count in 0..elements.length - 1
  #  puts "Count  #{count}: print #{entities[count]} now #{elements[count]["Anz."]} times"
  #  puts "entities: #{entities[count]}"
  #end
  #

  cl = ConnectionLoader.new(File.dirname(__FILE__) + "/classes/connection.ini")

  puts "Is the ConnectionLoaded successfully loaded? #{cl.isLoaded?}"
  # puts cl.materials
  cl.readElements

end
