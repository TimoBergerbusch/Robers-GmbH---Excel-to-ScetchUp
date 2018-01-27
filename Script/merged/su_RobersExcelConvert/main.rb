require 'inifile'
require 'rubyXL'
require_relative "classes/ExcelReader"
require_relative "classes/element_identifier"

class Main
  er = ExcelReader.new
  elements = er.loadDocument("real_test1.xlsm")

#elements = [{"Lfd"=>"1", "Sage Art."=>"10001317", "Bezeichnung"=>"DE", "Bauteil"=>"De Fläche -lan", "Materialgruppe"=>"PL", "Werkstoff"=>"OSB3 Platten 12mm", "Anz."=>"1", "Länge"=>"2524", "Breite"=>"1024", "Höhe"=>"12"},
#{"Lfd" => "2", "Sage Art." => "10001317", "Bezeichnung" => "SE", "Bauteil" => "Se Flächen !vertikal", "Materialgruppe" => "PL", "Werkstoff" => "OSB3 Platten 12mm", "Anz." => "2", "Länge" => "1646", "Breite" => "2524", "Höhe" => "12"},
#    {"Lfd" => "3", "Sage Art." => "10001317", "Bezeichnung" => "KO", "Bauteil" => "Ko Flächen !vertikal", "Materialgruppe" => "PL", "Werkstoff" => "OSB3 Platten 12mm", "Anz." => "2", "Länge" => "1000", "Breite" => "1731", "Höhe" => "12"},
#    {"Lfd" => "4", "Sage Art." => "10001596", "Bezeichnung" => "BO", "Bauteil" => "Bo Fläche -lan", "Materialgruppe" => "HO", "Werkstoff" => "Bretter, IPPC-HT-KD", "Anz." => "1", "Länge" => "2500", "Breite" => "1000", "Höhe" => "23"},
#    {"Lfd" => "5", "Sage Art." => "10001384", "Bezeichnung" => "BO", "Bauteil" => "Bo Querkantholz -lan", "Materialgruppe" => "HO", "Werkstoff" => "Kanthölzer, IPPC-HT-KD", "Anz." => "6", "Länge" => "1024", "Breite" => "100", "Höhe" => "100"},
#    {"Lfd" => "6", "Sage Art." => "10001596", "Bezeichnung" => "DE", "Bauteil" => "DeSp !que", "Materialgruppe" => "HO", "Werkstoff" => "Bretter, IPPC-HT-KD", "Anz." => "7", "Länge" => "800", "Breite" => "100", "Höhe" => "23"},
#    {"Lfd" => "7", "Sage Art." => "10001596", "Bezeichnung" => "DE", "Bauteil" => "DeSp -lan", "Materialgruppe" => "HO", "Werkstoff" => "Bretter, IPPC-HT-KD", "Anz." => "2", "Länge" => "2500", "Breite" => "100", "Höhe" => "23"}]

  ei = ElementIdentifier.new
  #puts ei.identifyElement(elements[0])
  entities = ei.identifyElements(elements)
  #puts entities
end
