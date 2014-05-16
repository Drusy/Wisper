package fr.wisper.camera;

public class MultipleVirtualViewportBuilder {

	private final float minWidth;
	private final float minHeight;
	private final float maxWidth;
	private final float maxHeight;

	public MultipleVirtualViewportBuilder(float minWidth, float minHeight, float maxWidth, float maxHeight) {
		this.minWidth = minWidth;
		this.minHeight = minHeight;
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
	}

	public VirtualViewport getVirtualViewport(float width, float height) {
		if (width >= minWidth && width <= maxWidth && height >= minHeight && height <= maxHeight)
			return new VirtualViewport(width, height, true);

		float aspect = width / height;

		float scaleForMinSize = minWidth / width;
		float scaleForMaxSize = maxWidth / width;

		float virtualViewportWidth = width * scaleForMaxSize;
		float virtualViewportHeight = virtualViewportWidth / aspect;

		if (insideBounds(virtualViewportWidth, virtualViewportHeight))
			return new VirtualViewport(virtualViewportWidth, virtualViewportHeight, false);

		virtualViewportWidth = width * scaleForMinSize;
		virtualViewportHeight = virtualViewportWidth / aspect;

		if (insideBounds(virtualViewportWidth, virtualViewportHeight))
			return new VirtualViewport(virtualViewportWidth, virtualViewportHeight, false);
		
		return new VirtualViewport(minWidth, minHeight, true);
	}
	
	private boolean insideBounds(float width, float height) {
		if (width < minWidth || width > maxWidth)
			return false;
		if (height < minHeight || height > maxHeight)
			return false;
		return true;
	}

}