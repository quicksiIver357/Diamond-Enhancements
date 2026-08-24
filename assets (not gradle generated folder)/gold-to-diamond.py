from PIL import Image
import sys
import os

def swap_red_blue(input_path):
    # Open the image in RGBA mode (converts palette and grayscale to full RGBA)
    img = Image.open(input_path).convert("RGBA")
    r, g, b, a = img.split()  # Split channels
    # Swap red and blue
    img = Image.merge("RGBA", (b, g, r, a))

    # Save new image
    base, ext = os.path.splitext(input_path)
    output_path = f"{base}_swapped{ext}"
    img.save(output_path)
    print(f"Processed {input_path}")
    print(f"Red and blue channels swapped. Saved to {output_path}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Drag and drop a PNG file onto this script.")
        sys.exit(1)
    input_file = sys.argv[1]
    if not os.path.isfile(input_file):
        print(f"File not found: {input_file}")
        sys.exit(1)
    swap_red_blue(input_file)