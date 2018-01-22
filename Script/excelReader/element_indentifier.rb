require 'inifile'

class ElementIndentifier

  def initialize
    initializeTransformations
    # change transformationsKey to unique identifier DeSpQ, etc...
    #TODO: read the translations from the iniFile
    @transformations = {
        "DE" => {"x-achse" => "Breite", "y-achse" => "Länge", "z-achse" => "Höhe"},
        "SE" => {"x-achse" => "Höhe", "y-achse" => "Breite", "z-achse" => "Länge"},
        "KO-V" => {"x-achse" => "Höhe", "y-achse" => "Breite", "z-achse" => "Länge"}, #TODO: false
        "BO" => {"x-achse" => "Höhe", "y-achse" => "Breite", "z-achse" => "Länge"} #TODO: false
    }
    #myini = IniFile.load('mytest.ini')
    #myini.each_section do |section|
    #  puts "I want #{myini[section]['somevar']} printed here!"
    #end
  end

  def identifyElements(elements)
    entities = []
    elements.each {|element|
      entity = identifyElement(element)
      if entity.is_a? nil::NilClass
        puts "ERROR: ElementIdentifier(Code: 0x03): Cannot identify all elements"
        return []
      end

      entities << entity
    }
    return entities
  end

  def identifyElement(element)
    x = y = z = 0

    key = getTransformationKey(element["Bezeichnung"], element["Bauteil"])

    if key.is_a?(nil::NilClass)
      puts "ERROR: ElementIdentifier(Code: 0x02): Cannot identify #{element}"
      return NilClass
    end

    x = element[@transformations[key]["x-achse"]].to_i
    y = element[@transformations[key]["y-achse"]].to_i
    z = element[@transformations[key]["z-achse"]].to_i


    #TODO: change to .mm
    return [x, y, z]
  end

  def getTransformationKey(bezeichnung, bauteil)

    puts "Bezeichnung: #{bezeichnung}, bauteil: #{bauteil}"
    myini = IniFile.load('translations.ini')
    myini.each_section do |section|
      if myini[section]["kuerzel"] == bezeichnung

        #TODO: erkennt DeSpQ auch als einfachen Deckell
        if myini[section]["bauteil"].is_a?(NilClass) ||
            bauteil.include?(myini[section]["bauteil"].force_encoding(::Encoding::UTF_8))
          puts "es passt #{myini[section]["key"]}"
          return @transformations[myini[section]["key"]]
          break
        end
      end
    end


    case bezeichnung
      when "SE"
        return "SE"
      when "DE"
        if bauteil.to_s.include?("quer")
          return "DeSpQ"
        else
          return "DE"
        end
      when "KO"
        if bauteil.to_s.include?("!vertikal")
          return "KO-V"
        else
          puts "ERROR: ElementIdentifier(Code: 0x04): No corresponding transformation key found for (#{bezeichnung}, #{bauteil})"
        end
      when "BO"
        return "BO"
      else
        puts "ERROR: ElementIdentifier(Code: 0x01): No corresponding transformation key found for (#{bezeichnung}, #{bauteil})"
        return NilClass
    end
  end
end