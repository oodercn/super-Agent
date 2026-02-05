# A2UI Skill References

This directory contains reference materials for the A2UI skill.

## Directory Structure

- `README.md` - This file, providing an overview of the references directory
- `examples/` - Example input/output pairs for the A2UI skill
- `schemas/` - JSON schemas for request/response formats
- `docs/` - Additional documentation files

## Usage

This directory is used by the A2UI skill to store reference materials and examples. It is not directly executed by the skill, but may be referenced by the documentation or scripts.

## Adding References

To add reference materials:

1. Create a subdirectory for your reference type (e.g., `examples/` for example inputs/outputs)
2. Add your reference files to the appropriate subdirectory
3. Update this README.md file if necessary

## Example Format

Example files should follow this format:

- `example-name-input.json` - Input parameters for the example
- `example-name-output.json` - Expected output for the example
- `example-name.md` - Description of the example

## Schema Format

JSON schemas should follow the JSON Schema Draft 2020-12 specification and validate the request/response formats for the A2UI skill API endpoints.
