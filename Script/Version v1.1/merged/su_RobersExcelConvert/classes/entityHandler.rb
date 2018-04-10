# The EntityHandler is used to store the rectangles created during the excel conversion.
# It is used instead drawing every rectangle for itself, but to draw them all at once.
class EntityHandler

  # constructor
  # parameter: -none-
  #
  # Initializes a new EntityHandler instance
  def initialize
    @model = Sketchup.active_model
    @rectangles = []
  end

  # method: createAndAddRectangle
  # parameter:
  #   p_height  integer(mm)       the height (z-axis)
  #   p_width   integer(mm)       the width (x-axis)
  #   p_depth   integer(mm)       the depth (y-axis)
  #   p_offset    Geom::Vector3d    the offset of the rectangle(mm)
  #   p_material  MaterialHandler   a MaterialHandler-instance
  # returns: -none-
  #
  # creates a new Rectangle and calls the addRectangle-method of this class
  def createAndAddRectangle(p_height, p_width, p_depth, p_offset, p_material)
    addRectangle(Rectangle.new(p_height, p_width, p_depth, p_offset, p_material))
  end

  # method: addRectangle
  # parameter:
  #   rectangle     Rectangle   a rectangle instance to add
  # returns: -none-
  #
  # adds a rectangle to the internal list of rectangles
  def addRectangle(rectangle)
    if rectangle.is_a? Rectangle
      @rectangles << rectangle
      rectangle.draw(@model)
    end
  end

  # method: drawAll
  # parameter: -none-
  # returns: -none-
  #
  # This method draws all of it's stored rectangles into the SketchUp model
  # calling the draw-method of every rectangle-instance
  def drawAll()
    @rectangles.each {|x|
      x.draw(@model)
    }
  end

  # method: length
  # parameter: -none-
  # returns: the length of the list
  def length
    return @rectangles.length
  end

  def deleteAll
    @rectangles.each{|rectangle|
      rectangle.delete
    }
    @rectangles=[]
  end
end
