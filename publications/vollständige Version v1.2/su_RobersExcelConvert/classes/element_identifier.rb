# The ElementIdentifier class is used to identify the mapping of the readable "Laenge", "Breite", "Hoehe" to the
# three-axis based on the "Kürzel" and "Bezeichnung/Bauteil".
class ElementIdentifier

  # constructor
  # parameter: -none-
  #
  # Initializes a new ElementIdentifier instance and tries to load the
  # "translations.ini"-file, in which the mappings are stored. The encoding is the following:
  #   [Kürzel]
  #   key = Kürzel
  #   name = a name to the Entity
  #   kuerzel = KE
  #   bauteil = a specialty to distinguish entities with the same Kuerzel
  #   x-achse = the entry corresponding to the x-axis
  #   y-achse = the entry corresponding to the y-axis
  #   z-achse = the entry corresponding to the z-axis
  #
  # The method calls the initializeTransformations-method.
  def initialize
    @transformations = Hash.new
    filePath = File.dirname(__FILE__) + "/translations.ini"
    if not File.exist? filePath then
      puts "ERROR: ElementIdentifier(Code: 0x05): NO translations.ini existing"
    end
    @file = IniFile.load(filePath)
    initializeTransformations
  end

  # method: initializeTransformations
  # parameter: -none-
  # returns: -none-
  #
  # This method reads all the sections of the "translations.ini" and registers them as a mapping based on
  # it's attributes.
  def initializeTransformations
    @file.clone.each_section do |section|
      @transformations[@file[section]["key"].to_s] = {"x-achse" => @file[section]["x-achse"],
                                                      "y-achse" => @file[section]["y-achse"],
                                                      "z-achse" => @file[section]["z-achse"]}
    end
  end

  # method: identifyElements
  # parameter:
  #   elements    Hash[]    the elements read from the excel
  # returns: an array of correctly transformed  coordinates
  def identifyElements(elements)
    entities = []
    elements.each_with_index {|element, index|
      #puts index
      entity = identifyElement(element)
      if entity.is_a? nil::NilClass
        puts "ERROR: ElementIdentifier(Code: 0x03): Cannot identify all elements"
        return []
      end

      entities << entity
    }
    return entities
  end

  # method: identifyElement
  # parameter:
  #   element   Hash    a mapping of the excel entries
  # returns: an array of correctly transformed coordinates for the given element
  def identifyElement(element)
    key = getTransformationKey(element["Bezeichnung"], element["Bauteil"])

    if key.is_a?(nil::NilClass)
      puts "ERROR: ElementIdentifier(Code: 0x02): Cannot identify #{element}"
      return NilClass
    end

    if @transformations[key].nil? then
      puts "ERROR Helper: element[Bezeichnung]= #{element["Bezeichnung"]}; element[Bauteil]=#{element["Bauteil"]}"
      puts "ERROR: ElementIdentifier(Code: 0x04):there is no transformation for the key=#{key}"
    end

    puts "Element: Materialgruppe = #{element["Materialgruppe"]} Werkstoff=#{element["Werkstoff"]}"

    x = element[@transformations[key]["x-achse"]].to_i
    y = element[@transformations[key]["y-achse"]].to_i
    z = element[@transformations[key]["z-achse"]].to_i

    return [x, y, z]
  end

  # method: getTransformationKey
  # parameter:
  #   bezeichnung   String    the "Kürzel" of the element
  #   bauteil       String    further information about the element
  # returns: the key to the corresponding entry within the transformations-hashmap
  def getTransformationKey(bezeichnung, bauteil)
    @file.each_section do |section|
      if @file[section]["kuerzel"] == bezeichnung

        #TODO: erkennt DeSpQ auch als einfachen Deckel
        if @file[section]["bauteil"].is_a?(nil::NilClass) ||
            bauteil.include?(@file[section]["bauteil"].force_encoding(::Encoding::UTF_8))
          return @file[section]["key"]
          break
        end
      end
    end
    puts "ERROR: ElementIdentifier(Code 0x06): No key to bezeichnung=#{bezeichnung} and bauteil=#{bauteil}"
  end

end