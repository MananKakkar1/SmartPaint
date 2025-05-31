# 🤖🎨 AI-Enhanced Paint Application

## 📝 Project Overview

This project is a **JavaFX desktop drawing program** that combines classic paint-like features with the power of generative AI! Users can draw on a canvas, manage custom save files, and even generate artwork by describing what they want in a text prompt—letting AI create structured images directly on the canvas.

## 🚀 What’s Implemented

- **JavaFX Drawing App** 🖌️  
  Intuitive GUI for freehand drawing, shapes, color selection, and more.

- **Custom Save Format** 💾  
  Drawings are saved and loaded in a strict `paintSaveFile` text format, ensuring compatibility and easy parsing.

- **AI-Generated Art** 🤖  
  Enter a prompt, and the app uses Llama3 to generate drawing instructions (not images!) that conform to the paintSaveFile format, which are then rendered on the canvas.

- **Finite State Machine Validation** 🧠  
  All AI-generated files and loaded files are validated with an FSM parser to ensure only well-formed files are accepted.

- **Undo/Redo & File Management** 🔄  
  Includes undo/redo, open, save, and new file features—making experimentation easy.

## 🎮 How It Works

1. **Draw Freely**: Use mouse and tools to create art directly on the canvas.
2. **Save/Load**: Export artwork to a `paintSaveFile`, or load it back later—always validated for correctness.
3. **AI Image Generation**: Type a description and let the built-in AI generate a structured paint file, instantly visualized in the app.
4. **Validation**: The FSM ensures every paint file (AI or user-made) is syntactically correct before rendering.

---

Experience classic painting with a futuristic twist: AI-generated, format-validated art you can save, share, and edit! 🖼️✨
