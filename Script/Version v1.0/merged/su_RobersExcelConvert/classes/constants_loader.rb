require "inifile"

class ConstantsLoader

  def initialize
    @constantsPath = File.dirname(__FILE__) + "/constants.ini"

    if not File.exist? @constantsPath then
      puts "ERROR: ElementIdentifier(Code: 0x05): NO translations.ini existing"
    end

    @constantsFile = IniFile.load(@constantsPath)

    setConstants
  end

  def setConstants()
    section = "RobersExcelConstants"
    $rowOfHeader = @constantsFile[section]["headerRow"]
    $indexValuesMap["Lfd"] = @constantsFile[section]["Lfd"]
    $indexValuesMap["Sage Art."] = @constantsFile[section]["SageArt"]
    $indexValuesMap["Bezeichnung"] = @constantsFile[section]["Bezeichnung"]
    $indexValuesMap["Bauteil"] = @constantsFile[section]["Bauteil"]
    $indexValuesMap["Materialgruppe"] = @constantsFile[section]["Materialgruppe"]
    $indexValuesMap["Werkstoff"] = @constantsFile[section]["Werkstoff"]
    $indexValuesMap["Anz."] = @constantsFile[section]["Anzahl"]
    $indexValuesMap["Laenge"] = @constantsFile[section]["Laenge"]
    $indexValuesMap["Breite"] = @constantsFile[section]["Breite"]
    $indexValuesMap["Hoehe"] = @constantsFile[section]["Hoehe"]
    puts "Finished Reading Constants"
  end
end